pipeline {
    agent any

	parameters {
		choice(
			name: 'ENVIRONMENT',
			choices: ['test', 'prod'],
			description: 'Choose the environment for deployment'
		)
	}

    environment {
        IMAGE_REGISTRY  = credentials('GITHUB_REGISTRY')
        IMAGE_TAG       = "${env.BUILD_NUMBER}"
        NAMESPACE       = "${params.ENVIRONMENT}"
        RELEASE         = "${params.ENVIRONMENT}"
        SERVICES        = "my-bank-front my-bank-cash my-bank-transfer my-bank-accounts my-bank-notifications"
    }

    stages {
        stage('Build JAR') {
			steps {
				sh 'mvn clean package'
			}
        }

        stage('Build Docker Images') {
            steps {
                sh '''
                for service in $SERVICES; do
                    docker build -t $IMAGE_REGISTRY/$service:$IMAGE_TAG -f Dockerfile ./$service
                done
                '''
            }
        }

		stage('Docker Login') {
			steps {
				withCredentials([
					string(credentialsId: 'GHCR_TOKEN', variable: 'GHCR_TOKEN'),
					string(credentialsId: 'GITHUB_USERNAME', variable: 'GITHUB_USERNAME')
				]) {
					sh 'echo $GHCR_TOKEN | docker login ghcr.io -u $GITHUB_USERNAME --password-stdin'
				}
			}
		}

        stage('Push Docker Images') {
            steps {
				sh '''
				for service in $SERVICES; do
					docker push $IMAGE_REGISTRY/$service:$IMAGE_TAG
				done
				'''
            }
        }

		stage('Production Approval') {
			when {
				expression { params.ENVIRONMENT == 'prod' }
			}
			steps {
				input message: 'Deploy to PROD environment?', ok: 'Yes, deploy'
			}
		}

        stage('Create K8S Namespace') {
			steps {
				sh 'kubectl create namespace $NAMESPACE --dry-run=client -o yaml | kubectl apply -f -'
			}
        }

        stage('Create K8S Secret') {
            steps {
            	withCredentials([
					usernamePassword(credentialsId: 'front-keycloak-creds', usernameVariable: 'FRONT_CLIENT_ID', passwordVariable: 'FRONT_CLIENT_SECRET'),
					usernamePassword(credentialsId: 'cash-keycloak-creds', usernameVariable: 'CASH_CLIENT_ID', passwordVariable: 'CASH_CLIENT_SECRET'),
					usernamePassword(credentialsId: 'transfer-keycloak-creds', usernameVariable: 'TRANSFER_CLIENT_ID', passwordVariable: 'TRANSFER_CLIENT_SECRET'),
					usernamePassword(credentialsId: 'accounts-keycloak-creds', usernameVariable: 'ACCOUNTS_CLIENT_ID', passwordVariable: 'ACCOUNTS_CLIENT_SECRET'),
					usernamePassword(credentialsId: 'accounts-db-creds', usernameVariable: 'ACCOUNTS_DB_USERNAME', passwordVariable: 'ACCOUNTS_DB_PASSWORD'),
					string(credentialsId: 'keycloak-admin-password', variable: 'KEYCLOAK_ADMIN_PASSWORD'),
					string(credentialsId: 'db-admin-password', variable: 'DB_ADMIN_PASSWORD'),
					string(credentialsId: 'keycloak-db-password', variable: 'KEYCLOAK_DB_PASSWORD')
                ]) {
						sh '''
						kubectl create secret generic my-bank-secret \
							--namespace $NAMESPACE \
							--from-literal=front.keycloak.client.id=$FRONT_CLIENT_ID \
							--from-literal=front.keycloak.client.secret=$FRONT_CLIENT_SECRET \
							--from-literal=cash.keycloak.client.id=$CASH_CLIENT_ID \
							--from-literal=cash.keycloak.client.secret=$CASH_CLIENT_SECRET \
							--from-literal=transfer.keycloak.client.id=$TRANSFER_CLIENT_ID \
							--from-literal=transfer.keycloak.client.secret=$TRANSFER_CLIENT_SECRET \
							--from-literal=accounts.keycloak.client.id=$ACCOUNTS_CLIENT_ID \
							--from-literal=accounts.keycloak.client.secret=$ACCOUNTS_CLIENT_SECRET \
							--from-literal=accounts.db.username=$ACCOUNTS_DB_USERNAME \
							--from-literal=accounts.db.password=$ACCOUNTS_DB_PASSWORD \
							--from-literal=keycloak.admin.password=$KEYCLOAK_ADMIN_PASSWORD \
							--from-literal=db.admin.password=$DB_ADMIN_PASSWORD \
							--from-literal=keycloak.db.user.password=$KEYCLOAK_DB_PASSWORD \
							--dry-run=client -o yaml | kubectl apply -f -
						'''
                }
            }
        }

		stage('Helm Dependency Update') {
			steps {
				sh '''
				for chart in ./my-bank ./my-bank/charts/*; do
					if [ -d "$chart" ]; then
						echo "Updating dependencies for $chart"
						helm dependency update "$chart"
					fi
				done
				'''
        	}
		}

        stage('Helm Deploy') {
            steps {
                sh '''
                helm upgrade --install $RELEASE ./my-bank \
                	--namespace $NAMESPACE \
					--set global.image.registry=$IMAGE_REGISTRY \
					--set global.image.tag=$IMAGE_TAG
                '''
            }
        }
    }

	post {
		always {
            sh '''
			for service in $SERVICES; do
				image="$IMAGE_REGISTRY/$service:$IMAGE_TAG"
				docker rmi "$image" || echo "Image \"$image\" not found"
			done

			docker image prune -f || true
            '''

            cleanWs()
		}
	}
}
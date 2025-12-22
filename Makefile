# чтобы команды всегда выполнялись, независимо от наличия файлов с такими же именами в директории
.PHONY: package build deploy up down clean

package:
	mvn clean package

build: package
	docker build -t my-bank-front -f Dockerfile ./my-bank-front
	docker build -t my-bank-cash -f Dockerfile ./my-bank-cash
	docker build -t my-bank-transfer -f Dockerfile ./my-bank-transfer
	docker build -t my-bank-accounts -f Dockerfile ./my-bank-accounts
	docker build -t my-bank-notifications -f Dockerfile ./my-bank-notifications

# -d означает без привязки к терминалу (detached)
up:
	docker-compose up -d postgres consul keycloak
	@echo "⏳ Ожидание запуска инфраструктуры..."
	@sleep 5
	docker compose up -d

deploy: build up

# для удаления volumes, связанных с сервисами в docker-compose, добавить флаг -v (полная очистка persistent data)
down:
	docker-compose down

restart: down up

clean:
	down
	docker rmi my-bank-front my-bank-cash my-bank-transfer my-bank-accounts my-bank-notifications || true
	mvn clean
document.addEventListener('DOMContentLoaded', () => {
    const errorAlert = document.getElementById('errorAlert');
    const infoAlert = document.getElementById('infoAlert');

    function hideAlert(alert) {
        if (!alert) return;

        alert.style.transition = 'opacity 0.4s ease-out';
        alert.style.opacity = '0';

        setTimeout(() => {
            alert.style.transition = 'all 0.5s ease-out';
            alert.style.maxHeight = '0';
            alert.style.marginBottom = '0';
            alert.style.padding = '0';
            alert.style.overflow = 'hidden';

            setTimeout(() => {
                alert.remove();
            }, 500);
        }, 400);
    }

    if (errorAlert) setTimeout(() => hideAlert(errorAlert), 5000);
    if (infoAlert) setTimeout(() => hideAlert(infoAlert), 3000);
});

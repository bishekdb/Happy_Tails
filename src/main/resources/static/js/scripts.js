// Wait for the DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    
    // Auto-dismiss alerts after 5 seconds
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            if (!alert.classList.contains('alert-permanent')) {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }
        });
    }, 5000);
    
    // File input preview for pet images
    const imageInput = document.getElementById('image');
    if (imageInput) {
        imageInput.addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                // Check if file is an image
                if (!file.type.match('image.*')) {
                    alert('Please select an image file (JPEG or PNG)');
                    imageInput.value = '';
                    return;
                }
                
                // Check file size (max 5MB)
                if (file.size > 5 * 1024 * 1024) {
                    alert('File size exceeds 5MB limit');
                    imageInput.value = '';
                    return;
                }
            }
        });
    }
    
    // Password matching validation for registration
    const passwordField = document.getElementById('password');
    const confirmPasswordField = document.getElementById('confirmPassword');
    const form = document.querySelector('form');
    
    if (passwordField && confirmPasswordField && form) {
        form.addEventListener('submit', function(event) {
            if (passwordField.value !== confirmPasswordField.value) {
                event.preventDefault();
                alert('Passwords do not match');
                confirmPasswordField.focus();
            }
        });
    }
    
    // Paw print animation
    const body = document.querySelector('body');
    
    body.addEventListener('mousemove', function(e) {
        // Only add paw prints 10% of the time to avoid too many elements
        if (Math.random() > 0.9) {
            createPawPrint(e.clientX, e.clientY);
        }
    });
    
    function createPawPrint(x, y) {
        const pawPrint = document.createElement('div');
        pawPrint.className = 'paw-print';
        pawPrint.style.left = (x - 10) + 'px';
        pawPrint.style.top = (y - 10) + 'px';
        
        // Random rotation for variety
        const rotation = Math.floor(Math.random() * 360);
        pawPrint.style.transform = `rotate(${rotation}deg)`;
        
        // Add to DOM
        document.body.appendChild(pawPrint);
        
        // Remove after animation completes
        setTimeout(() => {
            pawPrint.style.opacity = '0';
            setTimeout(() => {
                pawPrint.remove();
            }, 500);
        }, 1000);
    }
}); 
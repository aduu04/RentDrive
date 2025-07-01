// JS reserved for future interactivity
console.log("RentDrive script loaded.");

// Fallback if car image fails to load
function handleImageError(img) {
    img.onerror = null;
    img.src = '/images/default-car.png'; // Add a default image in static/images/
}

// Handle delete confirmation
function confirmDelete(button) {
    const carId = button.getAttribute('data-id');
    if (confirm('Are you sure you want to delete this car?')) {
        window.location.href = `/admin/delete-car/${carId}`;
    }
}

// Handle broken image fallback
function handleImageError(img) {
    img.src = '/images/default-car.png'; // Optional placeholder image
}

function confirmDelete(button) {
    const carId = button.getAttribute('data-id');
    if (confirm("Are you sure you want to delete this car?")) {
        window.location.href = `/admin/delete-car/${carId}`;
    }
}

function handleImageError(img) {
    img.src = "/images/default_car.jpg"; // optional default image fallback
}

function handleImageError(img) {
    img.src = "/images/default_car.jpg"; // use a fallback image if not found
}

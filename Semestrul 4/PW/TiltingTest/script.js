const container = document.querySelector('.container');
const image = document.getElementById('tiltImage');

// Calculate maximum rotation angle based on container dimensions
const maxRotation = 20;

container.addEventListener('mousemove', (e) => {
    const xPos = e.clientX - container.getBoundingClientRect().left - container.offsetWidth / 2;
    const yPos = e.clientY - container.getBoundingClientRect().top - container.offsetHeight / 2;
    const rotateX = -(yPos / container.offsetHeight / 2) * maxRotation; // Negative sign for correct direction
    const rotateY = (xPos / container.offsetWidth / 2) * maxRotation; // Positive sign for correct direction

    // Calculate scale based on maximum rotation angle
    const scaleFactorX = 1 + (Math.abs(rotateX) / maxRotation) * 0.2; // Scale factor for X-axis
    const scaleFactorY = 1 + (Math.abs(rotateY) / maxRotation) * 0.2; // Scale factor for Y-axis
    const scale = scaleFactorX * scaleFactorY; // Limit the maximum scale based on maxRotation

    // Apply rotation and scale transformation to the image
    image.style.transform = `rotateX(${rotateX}deg) rotateY(${rotateY}deg) scale(${scale}) translateZ(30px)`;
});

container.addEventListener('mouseleave', () => {
    // Reset transformation for image
    image.style.transform = 'rotateX(0) rotateY(0) scale(1) translateZ(0)';
});
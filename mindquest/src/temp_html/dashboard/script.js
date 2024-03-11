// Show/hide scroll to top button based on user scroll position
window.onscroll = function() {
  showScrollButton();
};

var isTransitioning = false;

function showScrollButton() {
  var scrollBtn = document.getElementById('scrollBtn');
  var currentPosition = document.documentElement.scrollTop || document.body.scrollTop;

  if (currentPosition > 20 && !isTransitioning) {
    if (!scrollBtn.style.display || scrollBtn.style.display === 'none') {
      isTransitioning = true;

      scrollBtn.style.display = 'block';
      setTimeout(() => {
        scrollBtn.style.opacity = '1';
        isTransitioning = false;
      }, 10);
    }
  } else {
    if (scrollBtn.style.display === 'block' && !isTransitioning) {
      isTransitioning = true;

      scrollBtn.style.opacity = '0';
      setTimeout(() => {
        scrollBtn.style.display = 'none';
        isTransitioning = false;
      }, 300); // Transition duration is 0.3s, so wait for that duration before hiding
    }
  }
}

// Smooth scroll to top function
function scrollToTop() {
  var currentPosition = document.documentElement.scrollTop || document.body.scrollTop;

  if (currentPosition > 0) {
    window.requestAnimationFrame(scrollToTop);
    window.scrollTo(0, currentPosition - currentPosition / 8);
  }
}
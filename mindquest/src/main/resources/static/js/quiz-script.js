document.addEventListener("DOMContentLoaded", function() {
  const questionSections = document.querySelectorAll('.question-section');
  let currentQuestionIndex = 0;

  // Function to show current question and update navigation buttons visibility
  function showQuestion(index) {
    questionSections.forEach((section, i) => {
      if (i === index) {
        section.style.display = 'flex';
      } else {
        section.style.display = 'none';
      }
    });

    // Hide/show next button based on current index
    const nextButton = document.querySelector('.next-button');
    nextButton.style.display = (index === questionSections.length - 1) ? 'none' : 'block';

    // Hide/show previous button based on current index
    const prevButton = document.querySelector('.prev-button');
    prevButton.style.display = (index === 0) ? 'none' : 'block';

    // Update index counter
    document.getElementById('counter').textContent = index + 1;

    // Change navigation-buttons class based on button visibility
    const navigationButtons = document.querySelector('.navigation-buttons');
    if (nextButton.style.display === 'none' || prevButton.style.display === 'none') {
      navigationButtons.classList.remove('half');
      navigationButtons.classList.add('full');
    } else {
      navigationButtons.classList.remove('full');
      navigationButtons.classList.add('half');
    }
  }

  // Show the first question initially
  showQuestion(currentQuestionIndex);

  // Function to handle next button click
  function onNextClick() {
    if (currentQuestionIndex < questionSections.length - 1) {
      currentQuestionIndex++;
      showQuestion(currentQuestionIndex);
    }
  }

  // Function to handle previous button click
  function onPrevClick() {
    if (currentQuestionIndex > 0) {
      currentQuestionIndex--;
      showQuestion(currentQuestionIndex);
    }
  }

  // Function to handle form submission
  function onSubmit() {
    // Check if there are any unanswered questions
    const unansweredQuestions = Array.from(questionSections).findIndex((section, index) => {
      const inputs = section.querySelectorAll('input[type="radio"]');
      return !Array.from(inputs).some(input => input.checked);
    });

    // If there are unanswered questions, jump to the first one
    if (unansweredQuestions !== -1) {
      currentQuestionIndex = unansweredQuestions;
      showQuestion(currentQuestionIndex);
    } else {
      // Otherwise, submit the form
      document.getElementById('quizForm').submit();
    }
  }

  // Add event listeners to next and previous buttons
  document.querySelector('.next-button').addEventListener('click', onNextClick);
  document.querySelector('.prev-button').addEventListener('click', onPrevClick);

  // Add event listener to form submission
  document.querySelector('.finish-button').addEventListener('click', onSubmit);
});
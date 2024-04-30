document.addEventListener("DOMContentLoaded", function() {
  const questionSections = document.querySelectorAll('.question-section');
  let currentQuestionIndex = 0;

  // Function to show current question and update navigation buttons visibility
  function showQuestion(index) {
    questionSections.forEach((section, i) => {
      if (i === index) {
        section.style.display = 'flex';
        // Show the "Please answer this question!" message if the current question is not answered
        const inputs = section.querySelectorAll('input[type="radio"]');
        const isQuestionAnswered = Array.from(inputs).some(input => input.checked);
        const questionMessage = section.querySelector('.question');
        if (isQuestionAnswered) {
          questionMessage.style.display = 'none';
        }
      } else {
        section.style.display = 'none';
      }
    });

    // Update index counter
    document.getElementById('counter').textContent = index + 1;

    // Disable/enable previous and next buttons based on current question index
    const prevButton = document.querySelector('.prev-button');
    const nextButton = document.querySelector('.next-button');
    prevButton.disabled = index === 0;
    nextButton.disabled = index === questionSections.length - 1;
    prevButton.style.cursor = index === 0 ? 'not-allowed' : 'pointer';
    nextButton.style.cursor = index === questionSections.length - 1 ? 'not-allowed' : 'pointer';
  }

  // Show the first question initially
  showQuestion(currentQuestionIndex);

  // Function to handle next button click
  function onNextClick() {
    // Check if the current question is answered
    const currentQuestionInputs = questionSections[currentQuestionIndex].querySelectorAll('input[type="radio"]');
    const isCurrentQuestionAnswered = Array.from(currentQuestionInputs).some(input => input.checked);

    // If the current question is answered, proceed to the next question
    if (isCurrentQuestionAnswered) {
      if (currentQuestionIndex < questionSections.length - 1) {
        currentQuestionIndex++;
        showQuestion(currentQuestionIndex);
      }
    }
    // If the current question is not answered, display the message
    else {
      const questionMessage = questionSections[currentQuestionIndex].querySelector('.question');
      questionMessage.style.display = 'block';
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
function decrease01(id) {
  var heightInput = document.getElementById(id);
  var currentValue = parseFloat(heightInput.value);
  var newValue = currentValue - 0.01; // Decrease by 0.01
  var maxValue = parseFloat(heightInput.max);
  var minValue = parseFloat(heightInput.min);
  var newValueClamped = Math.min(newValue, maxValue); // Clamp to the maximum value
  var newNewValueClamped = Math.max(newValueClamped, minValue);
  heightInput.value = newNewValueClamped.toFixed(2); // Round to 2 decimal places
}

function increase01(id) {
  var heightInput = document.getElementById(id);
  var currentValue = parseFloat(heightInput.value);
  var newValue = currentValue + 0.01; // Increase by 0.01
  var maxValue = parseFloat(heightInput.max);
  var minValue = parseFloat(heightInput.min);
  var newValueClamped = Math.min(newValue, maxValue); // Clamp to the maximum value
  var newNewValueClamped = Math.max(newValueClamped, minValue);
  heightInput.value = newNewValueClamped.toFixed(2); // Round to 2 decimal places
}

function decrease5(id) {
  var heightInput = document.getElementById(id);
  var currentValue = parseFloat(heightInput.value);
  var newValue = currentValue - 0.5; // Decrease by 0.5
  var maxValue = parseFloat(heightInput.max);
  var minValue = parseFloat(heightInput.min);
  var newValueClamped = Math.min(newValue, maxValue); // Clamp to the maximum value
  var newNewValueClamped = Math.max(newValueClamped, minValue);
  heightInput.value = newNewValueClamped.toFixed(1); // Round to 1 decimal place
}

function increase5(id) {
  var heightInput = document.getElementById(id);
  var currentValue = parseFloat(heightInput.value);
  var newValue = currentValue + 0.5; // Increase by 0.5
  var maxValue = parseFloat(heightInput.max);
  var minValue = parseFloat(heightInput.min);
  var newValueClamped = Math.min(newValue, maxValue); // Clamp to the maximum value
  var newNewValueClamped = Math.max(newValueClamped, minValue);
  heightInput.value = newNewValueClamped.toFixed(1); // Round to 1 decimal place
}

function decrease(id) {
  var heightInput = document.getElementById(id);
  var currentValue = parseInt(heightInput.value);
  var newValue = currentValue - 1; // Decrease by 1
  var maxValue = parseInt(heightInput.max);
  var minValue = parseInt(heightInput.min);
  var newValueClamped = Math.min(newValue, maxValue); // Clamp to the maximum value
  var newNewValueClamped = Math.max(newValueClamped, minValue);
  heightInput.value = newNewValueClamped;
}

function increase(id) {
  var heightInput = document.getElementById(id);
  var currentValue = parseInt(heightInput.value);
  var newValue = currentValue + 1; // Increase by 1
  var maxValue = parseInt(heightInput.max);
  var minValue = parseInt(heightInput.min);
  var newValueClamped = Math.min(newValue, maxValue);
  var newNewValueClamped = Math.max(newValueClamped, minValue);
  heightInput.value = newNewValueClamped;
}
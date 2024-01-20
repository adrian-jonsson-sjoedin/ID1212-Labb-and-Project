/*!
* Start Bootstrap - Modern Business v5.0.7 (https://startbootstrap.com/template-overviews/modern-business)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-modern-business/blob/master/LICENSE)
*/
// This file is intentionally blank
// Use this file to add JavaScript to your project
console.log('Hello from scripts.js');
var removedOption;

function updateCoopOptions() {
    // console.log("script triggered")

    // gives me the id for the student selected in the first drop down
    var student1 = document.getElementById('studentSelect').value;
    // console.log(student1);
    var coopSelect = document.getElementById('coopSelect');

    // console.log(coopSelect);

    // If an option was previously removed, add it back
    if (removedOption) {
        coopSelect.add(removedOption);
        removedOption = null;  // Reset the removed option
    }
    // // Remove options from the second dropdown
    if (coopSelect.options.length > 1) {
        var index = getOptionIndexByValue(coopSelect, student1);
        if (index !== -1) {
            removedOption = coopSelect.options[index];  // Store the removed option
            coopSelect.remove(index);
        }
    }
    // console.log(coopSelect);
}

function getOptionIndexByValue(selectElement, value) {
    for (var i = 0; i < selectElement.options.length; i++) {
        if (selectElement.options[i].value === value) {
            return i;
        }
    }
    return -1; // Return -1 if the option is not found
}

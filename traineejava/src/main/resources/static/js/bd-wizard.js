//Wizard Init

$("#wizard").steps({
    headerTag: "h3",
    bodyTag: "section",
    transitionEffect: "none",
    titleTemplate: '#title#'
});

//Form control

$('#categoryChoice1').on('change', function(e) {
    $('#enteredFirstName').text(e.target.value || 'Категория 1');
});

$('#categoryChoice2').on('change', function(e) {
    $('#enteredLastName').text(e.target.value || 'Категория 2');
});

$('#categoryChoice3').on('change', function(e) {
    $('#enteredPhoneNumber').text(e.target.value || 'Категория 3');
});

$('#categoryChoice4').on('change', function(e) {
    $('#enteredEmailAddress').text(e.target.value || 'Категория 4');
});

$('#time1').on('change', function(e) {
    $('#enteredTime1').text(e.target.value || '12.00');
});

$('#time2').on('change', function(e) {
    $('#enteredTime2').text(e.target.value || '12.00');
});

$('#range').on('change', function(e) {
    $('#enteredRange').text(e.target.value || '1.00');
});

$('#money').on('change', function(e) {
    $('#enteredMoney').text(e.target.value || '10.00');
});

$('#designation').on('change', function(e) {
    $('#enteredDesignation').text(e.target.value || 'Junior Developer');
});

$('#department').on('change', function(e) {
    $('#enteredDepartment').text(e.target.value || 'UI Development');
});

$('#employeeNumber').on('change', function(e) {
    $('#enteredEmployeeNumber').text(e.target.value || 'JDUI36849');
});

$('#workEmailAddress').on('change', function(e) {
    $('#enteredWorkEmailAddress').text(e.target.value || 'willms_abby@company.com');
});


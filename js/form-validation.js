/**
 * Restricts and formats license plate input to the strict AAAA-0000 pattern in real-time.
 * It also forces capital letters for the initial four characters.
 */
function validateLicensePlate(input) {
    let value = input.value.toUpperCase().replace(/[^A-Z0-9-]/g, '');
    let formattedValue = '';

    // Step 1: Process the first 4 characters (Letters only)
    let letters = value.slice(0, 4).replace(/[^A-Z]/g, '');
    formattedValue += letters;

    // Step 2: Add the hyphen if 4 letters have been entered
    if (formattedValue.length === 4 && value.length > 4) {
        formattedValue += '-';
    }

    // Step 3: Process the remaining 4 characters (Numbers only)
    if (formattedValue.length === 5) {
        let numbers = value.slice(5).replace(/[^0-9]/g, '');
        formattedValue += numbers.slice(0, 4);
    } else if (formattedValue.length > 5) {
        // This handles pasting large strings; we take the first 4 numbers after the hyphen
        let numbers = value.slice(formattedValue.length - 1).replace(/[^0-9]/g, '');
        formattedValue = formattedValue.slice(0, 5) + numbers.slice(0, 4);
    }

    // Final check to cut off anything extra
    if (formattedValue.length > 9) {
        formattedValue = formattedValue.slice(0, 9);
    }

    input.value = formattedValue;
}
function prepareDeleteModal(buttonElement) {
    const vehicleId = buttonElement.getAttribute('data-delete-id');
    const plateNumber = buttonElement.getAttribute('data-plate-number');

    document.getElementById('modal-plate-number-display').innerText = plateNumber;

    document.getElementById('vehicleIdToDelete').value = vehicleId;

    document.getElementById('deleteForm').action = '/car/delete/' + vehicleId;
}

document.addEventListener('DOMContentLoaded', function () {
    console.log("Modal Handler Script Loaded.");

    const modalElement = document.getElementById('deleteVehicleModal');

    if (modalElement) {
        modalElement.addEventListener('hidden.bs.modal', function () {
            document.body.classList.remove('modal-open');

            const backdrop = document.querySelector('.modal-backdrop');
            if (backdrop) {
                backdrop.remove();
            }
        });
    }

    const deleteButtons = document.querySelectorAll('.js-delete-trigger');

    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            prepareDeleteModal(this);

            const deleteModal = new bootstrap.Modal(modalElement);
            deleteModal.show();
        });
    });
});
document.addEventListener("DOMContentLoaded", function() {


    const searchInput = document.getElementById("browse-search-input");
    const vehicleContainer = document.getElementById("vehicle-list-container");

    const searchForm = document.getElementById("searchForm");
    const filterForm = document.getElementById("filterForm");

    const hiddenSearch = document.getElementById("filterFormSearch");


    if (searchInput && vehicleContainer) {
        searchInput.addEventListener("input", function() {
            const searchTerm = searchInput.value.toLowerCase().trim();
            const allVehicles = vehicleContainer.children;

            for (let i = 0; i < allVehicles.length; i++) {
                const vehicleCard = allVehicles[i];
                const cardText = vehicleCard.textContent.toLowerCase();

                if (cardText.includes(searchTerm)) {
                    vehicleCard.style.display = "";
                } else {
                    vehicleCard.style.display = "none";
                }
            }
        });

        searchInput.addEventListener("keydown", function(event) {
            if (event.key === "Enter") {
                event.preventDefault();
            }
        });
    }

    if (searchForm) {
        searchForm.addEventListener("submit", function() {

        });
    }

    if (filterForm) {
        filterForm.addEventListener("submit", function() {
            hiddenSearch.value = searchInput.value;
        });
    }

});
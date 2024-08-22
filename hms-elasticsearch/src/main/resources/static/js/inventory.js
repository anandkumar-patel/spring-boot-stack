document.addEventListener('DOMContentLoaded', function() {
    const searchBox = document.getElementById('search-box');
    const suggestions = document.getElementById('search-suggestions');
    const detailsTable = document.getElementById('details-table');

    searchBox.addEventListener('input', function() {
        const query = this.value;
        if (query.length > 0) {
            fetch(`/inventory/ajax?query=${query}`)
                .then(response => response.json())
                .then(data => {
                    suggestions.innerHTML = '';
                    data.forEach(item => {
                        const li = document.createElement('li');
                        li.textContent = item.name;
                        li.dataset.id = item.id;
                        li.addEventListener('click', () => {
                            fetch(`/inventory/details?id=${li.dataset.id}`)
                                .then(response => response.json())
                                .then(details => {
                                    if (details) {
                                        detailsTable.innerHTML = `
                                            <tr>
                                                <td>ID</td>
                                                <td>${details.id}</td>
                                            </tr>
                                            <tr>
                                                <td>Name</td>
                                                <td>${details.name}</td>
                                            </tr>
                                            <tr>
                                                <td>Description</td>
                                                <td>${details.description}</td>
                                            </tr>
                                            <tr>
                                                <td>Quantity</td>
                                                <td>${details.quantity}</td>
                                            </tr>
                                            <tr>
                                                <td>Location</td>
                                                <td>${details.location}</td>
                                            </tr>
                                            <tr>
                                                <td>SKU</td>
                                                <td>${details.sku}</td>
                                            </tr>
                                        `;
                                    } else {
                                        detailsTable.innerHTML = '<tr><td colspan="2">No details found</td></tr>';
                                    }
                                });
                        });
                        suggestions.appendChild(li);
                    });
                });
        } else {
            suggestions.innerHTML = '';
        }
    });
});

function areDetailsChanged() {
    let name = $("#name").val();
    let brand = $("#brand").val();
    let quantity = $("#quantity").val();

    let nameDefaultValue = $("#name").data("default-value");
    let brandDefaultValue = $("#brand").data("default-value");
    let quantityDefaultValue = $("#quantity").data("default-value");

    return name !== nameDefaultValue && nameDefaultValue !== undefined ||
        brand !== brandDefaultValue && nameDefaultValue !== undefined  ||
        quantity !== quantityDefaultValue && nameDefaultValue !== undefined;
}

function saveProduct() {
    let product_id = $("#product-ids").data("old-id");
    let name = $("#name").val();
    let brand = $("#brand").val();
    let quantity = $("#quantity").val();

    $.ajax({
        url: "update_product.php",
        method: "POST",
        data: {id: product_id, name: name, brand: brand, quantity: quantity},
        success: function(response) {
            alert("Product updated successfully!");
        },
        error: function(data) {
            alert(data.responseText);
        },
        complete: function(data) {
            console.log('completed:', data);
        }
    });
}

$("#product-ids")
    .load("product_ids.php")
    .on("change", function() {
        if (areDetailsChanged()) {
            let confirm = window.confirm("You have unsaved changes. Do you want to change the product?");
            if (confirm) {
                saveProduct();
            }
        }
        $(this).data("old-id", $(this).val());

        const product_id = $(this).val();
        $.get("product_info.php", {id: product_id}, function(data) {
            let name = $(data).find("name").text();
            let brand = $(data).find("brand").text();
            let quantity = $(data).find("quantity").text();

            $("#name").val(name).data("default-value", name);
            $("#brand").val(brand).data("default-value", brand);
            $("#quantity").val(quantity).data("default-value", quantity);

            $("#submit-button").prop("disabled", true);
        });
    });

$("#name, #brand, #quantity").on("input", function() {
    let isProductSelected = $("#product-ids").val() !== null;
    $("#submit-button").prop("disabled",
        !(areDetailsChanged()) || !isProductSelected);
});

$("#submit-button").on("click", function(event) {
    event.preventDefault();
    saveProduct();
});
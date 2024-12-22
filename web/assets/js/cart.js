/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

async function loadCartItems() {
    const response = await fetch(
            "LoadCartItems"
            );

    if (response.ok) {
        const json = await response.json();

        if (json.length == 0) {
            alert("Cart is empty");
            window.location = "/Molla/";
        } else {
            let cartItemContainer = document.getElementById('cart-item-container');
            let cartItemRow = document.getElementById('cart-item-row');

            document.getElementById('cart-item-quantity').innerHTML = '<input  type="number" class="form-control" min="1" step="1" data-decimals="0" required>';

            cartItemContainer.innerHTML = "";

            let total = 0;
            let totalDelivery = 0;

            json.forEach(item => {

                total += (item.product.price * item.qty);
                totalDelivery += item.product.delivery_fee;

                let cartItemRowClone = cartItemRow.cloneNode(true);
                cartItemRowClone.querySelector("#cart-item-a").href = "SingleProductView.html?id=" + item.product.id;
                cartItemRowClone.querySelector("#cart-item-image").src = "product_images/" + item.product.id + "/mainImage.png";
                cartItemRowClone.querySelector("#cart-item-a2").innerHTML = item.product.title;
                cartItemRowClone.querySelector("#cart-item-a2").href = "SingleProductView.html?id=" + item.product.id;
                cartItemRowClone.querySelector("#cart-item-price").innerHTML = "LKR " + item.product.price + ".00";
                cartItemRowClone.querySelector("#cart-item-quantity").innerHTML = "<input  type='number' class='form-control' value='" + item.qty + "' min='1' max='" + item.product.quantity + "' step='1' data-decimals='0' required>";
                cartItemRowClone.querySelector("#cart-item-subTotal").innerHTML = "LKR " + item.product.price * item.qty + ".00";

                cartItemContainer.appendChild(cartItemRowClone);
            });
            
            let cartTotal = total + totalDelivery;

            document.getElementById('cart-total').innerHTML = "LKR " + total + ".00";
            document.getElementById('cart-item-delivery').innerHTML = "LKR " + totalDelivery + ".00";
            document.getElementById('cart-total-amount').innerHTML = "LKR " + cartTotal + ".00";
        }

    } else {
        alert("Unable to handle the request");
    }
}
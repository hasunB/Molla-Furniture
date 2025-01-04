/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

var address;

payhere.onCompleted = function onCompleted(orderId) {
    alert("Order Placed Thank you.");
    windeow.location = "/Molla/";
    // Note: validate the payment and show success or failure page to the customer
};

// Payment window closed
payhere.onDismissed = function onDismissed() {
    // Note: Prompt user to pay again or show an error page
    console.log("Payment dismissed");
};

// Error occurred
payhere.onError = function onError(error) {
    // Note: show an error page
    console.log("Error:" + error);
};

async function loadData() {
    const response = await fetch("LoadCheckOut");

    if (response.ok) {
        const json = await response.json();
        console.log(json);

        if (json.success) {

            address = json.address;
            const cityList = json.cityList;
            const districtList = json.districtList;
            const provinceList = json.provinceList;
            const countryList = json.countryList;

            const cartList = json.cartList;

            //load counties
            let countrySelect = document.getElementById("select-country");
            countrySelect.length = 1;
            countryList.forEach(city => {

                let option = document.createElement("option");
                option.value = city.id;
                option.innerHTML = city.name;
                countrySelect.appendChild(option);
            });

            //load province
            let provinceSelect = document.getElementById("select-province");
            provinceSelect.length = 1;
            provinceList.forEach(city => {

                let option = document.createElement("option");
                option.value = city.id;
                option.innerHTML = city.name;
                provinceSelect.appendChild(option);
            });

            //load districts
            let districtSelect = document.getElementById("select-district");
            districtSelect.length = 1;
            districtList.forEach(city => {

                let Option = document.createElement("option");
                Option.value = city.id;
                Option.innerHTML = city.name;
                districtSelect.appendChild(Option);
            });

            //load cities
            let citySelect = document.getElementById("select-city");
            citySelect.length = 1;
            cityList.forEach(city => {

                let Option = document.createElement("option");
                Option.value = city.id;
                Option.innerHTML = city.name;
                citySelect.appendChild(Option);
            });

            let currentAddress = document.getElementById('checkout-diff-address');
            currentAddress.addEventListener("change", e => {

                let first_name = document.getElementById("fname");
                let last_name = document.getElementById("lname");
                let country = document.getElementById("select-country");
                let address1 = document.getElementById("address1");
                let address2 = document.getElementById("address2");
                let province = document.getElementById("select-province");
                let city = document.getElementById("select-city");
                let district = document.getElementById("select-district");
                let postal_code = document.getElementById("postal-code");
                let mobile = document.getElementById("mobile");
                let email = document.getElementById("email");
                let companyName = document.getElementById("company-name");
                if (currentAddress.checked) {

                    first_name.value = address.fname;
                    first_name.disabled = true;
                    last_name.value = address.lname;
                    last_name.disabled = true;
                    companyName.disabled = true;

                    city.value = address.city.id;
                    city.disabled = true;

                    country.value = address.country.id;
                    country.disabled = true;

                    province.value = address.province.id;
                    province.disabled = true;

                    district.value = address.district.id;
                    district.disabled = true;

                    address1.value = address.line1;
                    address1.disabled = true;
                    address2.value = address.line2;
                    address2.disabled = true;
                    postal_code.value = address.postalCode;
                    postal_code.disabled = true;
                } else {

                    first_name.value = "";
                    first_name.disabled = false;
                    last_name.value = "";
                    last_name.disabled = false;
                    companyName.disabled = false;

                    city.value = 0;
                    city.disabled = false;

                    country.value = 0;
                    country.disabled = false;

                    province.value = 0;
                    province.disabled = false;

                    district.value = 0;
                    district.disabled = false;

                    address1.value = "";
                    address1.disabled = false;
                    address2.value = "";
                    address2.disabled = false;
                    postal_code.value = "";
                    postal_code.disabled = false;
                }
            });

            //load cart items
            let checkOutItemConatiner = document.getElementById("checkout-item-container");

            let checkoutItem = document.getElementById("checkout-item");
            let checkoutSummerySubtotal = document.getElementById("checkout-summery-subtotal");
            let checkoutDelivery = document.getElementById("checkout-delivery");
            let checkoutSummeryTotal = document.getElementById("checkout-summery-total");

            checkOutItemConatiner.innerHTML = "";

            let sub_total = 0;
            let shipping_fee = 0;

            cartList.forEach(item => {

                let st_item_clone = checkoutItem.cloneNode(true);
                st_item_clone.querySelector("#checkout-item-title").innerHTML = item.product.title;
                st_item_clone.querySelector("#checkout-item-qty").innerHTML = item.qty;

                let item_sub_total = item.product.price * item.qty;
                sub_total += item_sub_total;

                shipping_fee += item.product.delivery_fee;

                st_item_clone.querySelector("#checkout-item-price-subtotal").innerHTML = "LKR " + item_sub_total + ".00";

                checkOutItemConatiner.appendChild(st_item_clone);

            });

            checkoutSummerySubtotal.innerHTML = "<td>Subtotal:</td>\n\
                                                 <td>LKR " + sub_total + ".00</td>";
            checkOutItemConatiner.appendChild(checkoutSummerySubtotal);
            checkoutDelivery.innerHTML = "<td>Shipping:</td>\n\
                                          <td>LKR " + shipping_fee + ".00</td>";
            checkOutItemConatiner.appendChild(checkoutDelivery);
            checkoutSummeryTotal.innerHTML = "<td>Total:</td>\n\
                                              <td>LKR " + (sub_total + shipping_fee) + ".00</td>";
            checkOutItemConatiner.appendChild(checkoutSummeryTotal);
        } else {
            alert("Please LogIn to place Your Order");
            windeow.location = "/Molla/";
        }
    } else {
        alert("Something Went Wrong Plase try Again Later");
    }
}

async function checkout() {

    let isCurrentAddress = document.getElementById("checkout-diff-address").checked;

    //get address
    let first_name = document.getElementById("fname");
    let last_name = document.getElementById("lname");
    let country = document.getElementById("select-country");
    let address1 = document.getElementById("address1");
    let address2 = document.getElementById("address2");
    let province = document.getElementById("select-province");
    let city = document.getElementById("select-city");
    let district = document.getElementById("select-district");
    let postal_code = document.getElementById("postal-code");
    let companyName = document.getElementById("company-name");

    //request data(json)
    const data = {
        isCurrentAddress: isCurrentAddress,
        first_name: first_name.value,
        last_name: last_name.value,
        city_id: city.value,
        address1: address1.value,
        address2: address2.value,
        postal_code: postal_code.value,
        country: country.value,
        province: province.value,
        district: district.value,
        companyName: companyName.value
    };

    const response = await fetch(
            "Checkout",
            {
                method: "POST",
                body: JSON.stringify(data),
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();

        if (json.success) {
            
            payhere.startPayment(json.payhereJson);
            console.log(json.payhereJson);
        } else {
            alert(json.message);
        }

    } else {
        alert("Please try again");
    }

}

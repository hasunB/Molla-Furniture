/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

async function loadProduct() {

    const parameters = new URLSearchParams(window.location.search);
    if (parameters.has("id")) {
        const productId = parameters.get("id");
        const response = await fetch("LoadSingleProduct?id=" + productId);
        if (response.ok) {
            const json = await response.json();
            const id = json.product.id;
            document.getElementById('breadcrumb-item').innerHTML = json.product.category.name;
            document.getElementById('breadcrumb-item-active').innerHTML = json.product.productTypeHasBrand.productType.name;
            document.getElementById('product-zoom').src = "product_images/" + id + "/mainImage.png";
            document.getElementById('mainImage').src = "product_images/" + id + "/mainImage.png";
            document.getElementById('image1').src = "product_images/" + id + "/image1.png";
            document.getElementById('image2').src = "product_images/" + id + "/image2.png";
            document.getElementById('image3').src = "product_images/" + id + "/image3.png";
            document.getElementById('product-title').innerHTML = json.product.title;
            document.getElementById('product-price').innerHTML = "LKR " + json.product.price + ".00";
            let description = json.product.description;
            document.getElementById('product-content').innerHTML = description.split(" ").slice(0, 15).join(" ");
            document.getElementById('product-color').style.background = json.product.color.name;
            document.getElementById('product-quantity').innerHTML = json.product.quantity + " Avaliable in Stock";
            document.getElementById('footer-category').innerHTML = json.product.category.name;
            document.getElementById('footer-description').innerHTML = description;
            document.getElementById('qty').setAttribute("max", json.product.quantity);
            document.getElementById('add-to-cart-main').addEventListener("click", (e) => {
                addToCart(id, document.getElementById('qty').value);
                e.preventDefault();
            });
            let ProductHtml = document.querySelector('#smiilar-products');
            document.getElementById("similiar-product-main").innerHTML = "";
            json.productList.forEach(item => {
//                let similarProductContent = document.querySelector('#smiilar-products');
                let productClonehtml = ProductHtml.cloneNode(true);
                productClonehtml.querySelector("#similar-product-image").src = "product_images/" + item.id + "/mainImage.png";
                productClonehtml.querySelector("#similar-product-type").innerHTML = item.productTypeHasBrand.productType.name;
                productClonehtml.querySelector("#similar-product-link").href = "SingleProductView.html?id=" + item.id;
                productClonehtml.querySelector("#similar-product-link2").innerHTML = item.title;
                productClonehtml.querySelector("#similar-product-price").innerHTML = "LKR " + item.price + ".00";
                productClonehtml.querySelector("#similar-product-link2").href = "SingleProductView.html?id=" + item.id;
                productClonehtml.querySelector("#similar-product-color").style.background = item.color.name;
                document.getElementById('similiar-product-main').appendChild(productClonehtml);
                console.log(item.id);
            });
            // Product Gallery - product-gallery.html 
            $('.similiar-product-main').owlCarousel({
                loop: false,
                margin: 20,
                responsiveClass: false,
                nav: false,
                navText: ['<i class="icon-angle-left">', '<i class="icon-angle-right">'],
                dots: false,
                smartSpeed: 400,
                autoplay: true,
                autoplayTimeout: 15000,
                responsive: {
                    0: {
                        items: 1
                    },
                    480: {
                        items: 2
                    },
                    768: {
                        items: 3
                    },
                    992: {
                        items: 4
                    },
                    1200: {
                        items: 4,
                        nav: true,
                        dots: false
                    }
                }
            });
        } else {
            window.location = "/Molla/";
        }
    } else {
        window.location = "404.html";
    }


}


async function addToCart(id, qty) {

    const response = await fetch(
            "AddToCart?id=" + id + "&qty=" + qty
            );

    if (response.ok) {
        const json = await response.json();
        if (json.success) {
            alert(json.content);
        } else {
            alert(json.content);
        }
    } else {
        alert("Unable to handle the the Request");
    }
}

/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

async function checkSignIn() {

    const response = await fetch("CheckSignIn");

    if (response.ok) {

        const json = await response.json();

        var loadQuickLink = document.getElementById('load-quick-link');

        const response_DTO = json.response_DTO;

        if (response_DTO.success) {

            //sign in
            const user = response_DTO.content;

            loadQuickLink.innerHTML = "<li><a href='tel:#'><i class='icon-phone'></i>Call: +0123 456 789</a></li>\n\
                                       <li><a href='wishlist.html'><i class='icon-heart-o'></i>My Wishlist <span>(0)</span></a></li>\n\
                                       <li><a href='about.html'>About Us</a></li><li><a href='contact.html'>Contact Us</a></li>\n\
                                       <li><a href='about.html'>" + user.fname + " " + user.lname + "</a></li>\n\
                                       <li><a href='Logout'></i>Logout</a></li>";

        } else {
            //Not signIn
            console.log("not SignIN");
            loadQuickLink.innerHTML = "<li><a href='tel:#'><i class='icon-phone'></i>Call: +0123 456 789</a></li>\n\
                                       <li><a href='wishlist.html'><i class='icon-heart-o'></i>My Wishlist <span>(0)</span></a></li>\n\
                                       <li><a href='about.html'>About Us</a></li><li><a href='contact.html'>Contact Us</a></li>\n\
                                       <li><a href='#signin-modal' data-toggle='modal'><i class='icon-user'></i>Login</a></li>";
        }

        let ProductHtml = document.querySelector('#home-product-item');
        document.getElementById("home-latest-product-list").innerHTML = "";

        const productList = json.products;
        console.log(productList);

        productList.forEach(product => {
//           console.log(product.id);
            let productClonehtml = ProductHtml.cloneNode(true);
            productClonehtml.querySelector(".product-image").src = "product_images/" + product.id + "/mainImage.png";
            productClonehtml.querySelector(".product-image-hover").src = "product_images/" + product.id + "/image1.png";
            productClonehtml.querySelector(".product-title").innerHTML = product.title;
            productClonehtml.querySelector("#home-product-addToCart").href = "SingleProductView.html?id=" + product.id;
//            productClonehtml.querySelector("#similar-product-link2").innerHTML = item.title;
            productClonehtml.querySelector(".product-price").innerHTML = "LKR " + product.price + ".00";
//            productClonehtml.querySelector("#similar-product-link2").href = "SingleProductView.html?id=" + item.id;
//            productClonehtml.querySelector("#similar-product-color").style.background = item.color.name;
            document.getElementById('home-latest-product-list').appendChild(productClonehtml);
        });
        // Product Gallery - product-gallery.html 
        $('.home-latest-product-list').owlCarousel({
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

    }

}
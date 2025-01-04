/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

var brandList;
var productTypeList;
var productTypeHasBrandList;

async function loadFeatures() {

    const response = await fetch(
            "LoadFeatures",
            );

    if (response.ok) {
        const json = await response.json();

        const categoryList = json.categoryList;
        productTypeList = json.productTypeList;
        const colorList = json.colorList;
        brandList = json.brandList;
        productTypeHasBrandList = json.productTypeHasBrandList;

        createOptions("category-select", categoryList, ["id", "name"]);
        createOptions("color-select", colorList, ["id", "name"]);

    } else {
        alert("Something Went Wrong");
    }
}


function createOptions(id, Object, propertyArray) {
    const categorySelect = document.getElementById(id);
    Object.forEach(item => {
        let optionTag = document.createElement("option");
        optionTag.value = item[propertyArray[0]];
        optionTag.innerHTML = item[propertyArray[1]];
        categorySelect.appendChild(optionTag);
    });
}

function updateProductType() {

    let productTypeTag = document.getElementById("productType-select");
    productTypeTag.length = 1;

    let selectedCategoryId = document.getElementById("category-select").value;

    productTypeList.forEach(item => {
        if (item.category.id == selectedCategoryId) {
            let optionTag = document.createElement("option");
            optionTag.value = item.id;
            optionTag.innerHTML = item.name;
            productTypeTag.appendChild(optionTag);
        }
    });
}

function updateBrand() {
    let brandTag = document.getElementById("brand-select");
    brandTag.length = 1;

    let productTypeId = document.getElementById("productType-select").value;

    productTypeHasBrandList.forEach(item => {
        if (item.productType.id == productTypeId) {
            brandList.forEach(item2 => {
                if (item2.id == item.brand.id) {
                    let optionTag = document.createElement("option");
                    optionTag.value = item2.id;
                    optionTag.innerHTML = item2.name;
                    brandTag.appendChild(optionTag);
                }
            });
        }
    });

}

async function productListing() {
    const categorySelectTag = document.getElementById('category-select');
    const productTypeTag = document.getElementById('productType-select');
    const brandTag = document.getElementById('brand-select');
    const colorTag = document.getElementById('color-select');
    const titleTag = document.getElementById('title');
    const description = document.getElementById('description');
    const quantityTag = document.getElementById('quantity');
    const priceTag = document.getElementById('price');
    const mainImage = document.getElementById('mainImage');
    const Image1 = document.getElementById('image1');
    const Image2 = document.getElementById('image2');
    const Image3 = document.getElementById('image3');

    const data = new FormData();
    data.append("categoryId", categorySelectTag.value);
    data.append("productTypeId", productTypeTag.value);
    data.append("brandId", brandTag.value);
    data.append("colorId", colorTag.value);
    data.append("title", titleTag.value);
    data.append("description", description.value);
    data.append("quantity", quantityTag.value);
    data.append("price", priceTag.value);
    data.append("mainImage", mainImage.files[0]);
    data.append("image1", Image1.files[0]);
    data.append("image2", Image2.files[0]);
    data.append("image3", Image3.files[0]);

    const response = await fetch(
            "ProductListing",
            {
                method: "POST",
                body: data
            }
    );
    
    const messageTag = document.getElementById('productErrorMsg');

    if (response.ok) {
        const json = await response.json();

        if (json.success) {
            categorySelectTag.value = 0;
            productTypeTag.length = 1;
            brandTag.length = 1;
            titleTag.value = "";
            description.value = "";
            colorTag.value = 0;
            priceTag.value = "";
            quantityTag.value = 1;
            mainImage.value = null;
            Image1.value = null;
            Image2.value = null;
            Image3.value = null;

            messageTag.innerHTML = json.content;
            messageTag.style.color = "green";
        } else {
            messageTag.innerHTML = json.content;
        }
    } else {
        messageTag.innerHTML = "Please Try Again Later!";
    }
}



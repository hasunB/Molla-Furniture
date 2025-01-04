/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


async function loadData() {

    const response = await fetch("AdvancedSearch");

    if (response.ok) {

        const data = await response.json();
        console.log(data);
    } else {
        alert("Something Went Wrong Please Try Again Later");
    }

}



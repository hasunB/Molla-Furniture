/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

async function verifyAccount() {

    const user_dto = {
        verification: document.getElementById("verification").value
    };

    console.log(user_dto);

    const response = await fetch(
            "VerifyAccount",
            {
                method: "POST",
                body: JSON.stringify(user_dto),
                headers: {
                    "Content-Type": "application/json"

                }
            }
    );

    if (response.ok) {
        const json = await response.json();
        if (json.success) {
            window.location = '/Molla/';
        } else {
            document.getElementById('errorMsg').innerHTML = json.content;
        }
    } else {
        document.getElementById('errorMsg').innerHTML = "Please Try Again Later!";
    }
}




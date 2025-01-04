/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

async function logIn() {

    const user_dto = {
        email: document.getElementById("singin-email").value,
        password: document.getElementById("singin-password").value
    };

    console.log(user_dto);

    const response = await fetch(
            "LogIn",
            {
                method: "POST",
                body: JSON.stringify(user_dto),
                headers: {
                    "Content-Type": "application/json"

                }
            }
    );
    
    if(response.ok){
        const json = await response.json();
        if(json.success){
            window.location = '/Molla/';
        } else {
            
            if(json.content == "UnVerified"){
                window.location = 'Verification.html';
            } else {
                document.getElementById('signInError').innerHTML = json.content;
            }
            
            document.getElementById('signInError').innerHTML = json.content;
        }
    } else {
        document.getElementById('signInError').innerHTML = "Please Try Again Later!";
    }
}




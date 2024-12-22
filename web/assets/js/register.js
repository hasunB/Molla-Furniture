async function register() {

    const user_dto = {
        fname: document.getElementById("first-name").value,
        lname: document.getElementById("last-name").value,
        email: document.getElementById("register-email").value,
        password: document.getElementById("register-password").value
    };

    console.log(user_dto);

    const response = await fetch(
            "Register",
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
            window.location = 'Verification.html';
        } else {
            document.getElementById('errorMsg').innerHTML = json.content;
        }
    } else {
        document.getElementById('errorMsg').innerHTML = "Please Try Again Later!";
    }
}


// On login/signup page
    const currentUrl = window.location.href;
    // Store currentUrl in session or localStorage
    sessionStorage.setItem('user.html', 'login&signup.html');

    // On successful login/signup
    const redirectUrl = sessionStorage.getItem('user.html');
    if (redirectUrl) {
        window.location.href = redirectUrl;
    }
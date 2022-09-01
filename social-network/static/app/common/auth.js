window.getCurrentUser =  function getCurrentUser(){
    let user = localStorage.getItem('user');
    if (user !== null)
        return JSON.parse(user);
    return null;
}
window.getToken = function getToken() {
    return localStorage.getItem('jwt');
}
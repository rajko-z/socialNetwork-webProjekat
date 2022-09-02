Vue.component("home-page-admin", {
    data: function () {
        return {
            loggedUser: window.getCurrentUser(),
        }
    },

    template: `
<div>
    <h1>ADMIN HOME PAGE - {{loggedUser.username}}</h1>
</div>
    `
    ,
    mounted() {
        if (window.getCurrentUser() === null) {
            router.push('/');
        }
        if (window.getCurrentUser().role !== 'ADMIN') {
            alert('Unauthorized');
            router.push('homePageRegular');
        }
    }

});

Vue.component("edit-profile-page", {
    data: function () {
        return {
            loggedUser: window.getCurrentUser()
        }
    },

    mounted() {
        if (window.getCurrentUser() === null) {
            router.push('/');
        }
    },

    methods: {
    },


    template: `
<div>
    <h2>EDIT PROFILE PAGE</h2>
    <edit-profile-image></edit-profile-image>
    <edit-basic-info></edit-basic-info>
    
</div>
    `


});
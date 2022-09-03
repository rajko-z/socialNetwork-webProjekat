
Vue.component("user-profile-page", {
    data: function () {
        return {
            user: null
        }
    },

    methods: {
    },

    mounted: function() {
        let username = this.$route.params.username;
        window.API.get("users/" + username).then(res => {
            this.user = res.data;
        }).catch(err => {
            alert(err.response.data);
        })
    }

    ,

    template: `
<div class="">
    <user-info :user="user"></user-info>
</div>
    `


});
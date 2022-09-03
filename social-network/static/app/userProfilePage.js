
Vue.component("user-profile-page", {
    data: function () {
        return {
            user: null
        }
    },

    methods: {
        loadUser: function() {
            let username = this.$route.params.username;
            window.API.get("users/" + username).then(res => {
                this.user = res.data;
            }).catch(err => {
                alert(err.response.data);
            });
        }
    },

    mounted: function() {
        let username = this.$route.params.username;
        window.API.get("users/" + username).then(res => {
            this.user = res.data;
        }).catch(err => {
            alert(err.response.data);
        });
    }
    //
    // watch: {
    //     '$route.params': this.loadUser(),
    // }

    ,

    template: `
<div>
    <user-info :user="user" view="big"></user-info>
    <br/>
    <br/>
    <friend-list :username="user.username"></friend-list>
</div>
    `


});

Vue.component("message-page", {
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
    ,

    template: `
<div>
    <div style="display: flex;flex-direction: row;">
        <friend-list :username="user.username"></friend-list>
    </div>
</div>
    `


});
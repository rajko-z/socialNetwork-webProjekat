
Vue.component("user-profile-page", {
    data: function () {
        return {
            user: null,
            loggedUser : window.getCurrentUser(),
        }
    },

    methods: {
        loadUser: function() {
            let username = this.$route.params.username;
            window.API.get("users/" + username).then(res => {
                this.user = res.data;
                console.log(me);
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
        console.log(this.user);
    }
    ,

    template: `
<div>
    <user-info :user="user" view="big"></user-info>
    <br/>
    <br/>
    <div style="display: flex;flex-direction: row;">
        <friend-list v-if="loggedUser != null" :username="user.username" ></friend-list>
        <post-list :user="user"></post-list>
    </div>
</div>
    `


});
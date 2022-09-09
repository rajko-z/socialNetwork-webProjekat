
Vue.component("message-page", {
    data: function () {
        return {
            user: null,
            logUsername: null,


        }
    },


    mounted: function() {
        let username = this.$route.params.username;
        window.API.get("users/" + username).then(res => {
            this.user = res.data;
            console.log(this.user);
            this.logUsername =window.getCurrentUser();



        }).catch(err => {
            alert(err.response.data);
        });
    },


    template: `
<div>
    <div style="display: flex;flex-direction: row;">
        <friend-list :username="logUsername.username"></friend-list>
        <chat-component :userTo="user.username"></chat-component>
     
    </div>
</div>
    `


});

Vue.component("message-form-admin", {
    data: function () {
        return {
            user: null,
            logUsername:  window.getCurrentUser().username


        }
    },
    mounted: function() {

        console.log(this.logUsername);

    },





    template: `
<div>
<h1>Messages from admin</h1>
    <div style="display: flex;flex-direction: row;">
        <chat-component :userTo="logUsername" :fromAdmin = "true"></chat-component>
     
    </div>
</div>
    `


});
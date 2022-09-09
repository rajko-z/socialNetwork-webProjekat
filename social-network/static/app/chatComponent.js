Vue.component("chat-component", {
    props: [ 'userTo'],
    data: function () {
        return {
            user: null,
            loggedUser: window.getCurrentUser(),
            chat: [],
            userTo: null,
            userToUsername: this.userTo,
            messageText: null

        }
    },

    computed: {
        numOfMessages() {
            return this.chat.length;
        }
    },
    methods: {
        sendMessage: function() {
            if (this.messageText == null || this.messageText.trim() === '') {
                alert("You can't send empty message");
                return;
            }
            let newMessage = {
                text: this.messageText,
                username: this.userToUsername
            };
            window.API.post("sendMessageTo", newMessage).then(res => {
                this.chat.push(res.data);
                this.messageText = null;
            }).catch(err => {
                alert(err.response.data);
            })
        },


    },


    mounted: function() {
        if (window.getCurrentUser() === null) {
            router.push('/');
        }
        window.API.get("users/" + this.userToUsername).then(res => {
            this.userTo = res.data;
        }).catch(err => {
            alert(err.response.data);

        });


            window.API.get("messages/"+this.loggedUser.username+"/"+this.userTo).then(res => {
                this.chat = res.data;

            }).catch(err => {
                alert(err.response.data);
            })
        console.log(this.chat);

    }
    ,


    template: `
 
    <div class="card postCardContainer chatBox">
        <div class="card-header postCommentsContainer">
            <h8>Messages with {{userToUsername}} ({{numOfMessages}})</h8>
            <hr>
            <br/>
            <div v-for="message in chat" class="d-flex justify-content-between align-items-center">

               <message-component :message = "message"></message-component>
            </div>
            <div v-if="loggedUser != null" class="d-flex justify-content-between align-items-center postMessageContainer">
            <div class="d-flex justify-content-between align-items-center">
                <div class="mr-2">
                    <img class="profileImageIconSmall" v-bind:src="loggedUser.imageUrl" alt="">
                </div>
                <div class="ml-2">
                    <textarea class="messageInputField" v-model="messageText" placeholder="Write message here..."/>
                </div>
                <button v-on:click="sendMessage()" class="btn btn-primary">Send</button>
            </div>
        </div>
        </div>   
    </div>
 
    `


});
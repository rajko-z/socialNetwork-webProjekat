
Vue.component("comment-component", {
    props: ['comment'],
    data: function () {
        return {
            user: null,
            editActive: false,
            newText: this.comment.text,
            savedText: null,
            loggedUser: window.getCurrentUser()
        }
    },

    methods: {

        editComment: function() {
            this.editActive = true;
        },

        saveEditedComment: function () {
            if (this.newText === this.comment.text) {
                alert("Please make some changes to the comment before save");
                return;
            }
            if (this.newText == null || this.newText.trim() === '') {
                alert("I know you are speechless, but you cant post empty comment");
                return;
            }
            let newComment = {
                text: this.newText,
                commentId: this.comment.id
            };
            this.savedText = this.newText;
            this.editActive = false;
            this.$parent.saveEditedComment(newComment);
        },

        backToPrevious: function () {
            this.editActive = false;
            this.newText = this.comment.text;
        },

        removeComment: function () {
            this.$parent.removeComment(this.comment);
        }
    },

    template: `
    
    <div class="d-flex justify-content-between align-items-center">
        <div class="mr-2">
            <img class="profileImageIconSmall" v-bind:src="comment.user.profileImageUrl" alt="">
        </div>
        <div class="ml-2">
            <div class="h6 m-0">
                @{{comment.user.username}}
            </div>
            
            <span v-if="comment.modifiedAt" class="text-muted h8 mb-2" style="font-size: 9px"> <i class="fa fa-clock-o"></i>{{comment.modifiedAt}}</span>
            <span v-else class="text-muted h8 mb-2" style="font-size: 9px"> <i class="fa fa-clock-o"></i>{{comment.createdAt}}</span>
            
            <br/>
        </div>
        <div v-if="editActive">
            <div class="ml-2">
                <textarea class="commentInputField" autofocus v-bind:value="comment.text" v-model="newText"/>
            </div>
            <button v-on:click="saveEditedComment()" class="bnt btn-primary">Save</button>
            <button v-on:click="backToPrevious()" class="bnt btn-primary">Discard</button>
        </div>
        <div v-else-if="savedText">
            <div class="ml-2">
                <textarea class="commentInputField" disabled v-bind:value="savedText"/>
            </div>
        </div>
        <div v-else>
            <div class="ml-2">
                <textarea class="commentInputField" disabled v-bind:value="comment.text"/>
            </div>
        </div>
        
        
        <div v-if="loggedUser != null && loggedUser.username === comment.user.username">
            <div class="dropdown">
                <button class="btn btn-link dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                </button>
                <div class="dropdown-menu dropdown-menu-right">
                    <div><button v-on:click="editComment(comment)" class="dropdown-item editCommentMenuItem fa fa-pencil" > Edit</button></div>
                    <hr/>
                    <div><button v-on:click="removeComment()" class="dropdown-item deleteCommentMenuItem fa fa-remove"> Delete</button></div>
                </div>
            </div>
        </div>
    </div>
    `


});
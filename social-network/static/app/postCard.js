Vue.component("post-card", {
    props: ['post', 'user'],
    data: function () {
        return {
            user: null,
            loggedUser: window.getCurrentUser(),
            commentText: null,
            comments: this.post.comments,
            textDel: null,

        }
    },

    computed: {
        numOfComments() {
            return this.comments.length;
        },

    },
    methods: {
        addComment: function() {
            if (this.commentText == null || this.commentText.trim() === '') {
                alert("I know you are speechless, but you cant post empty comment");
                return;
            }
            let newComment = {
                text: this.commentText,
                postId: this.post.id
            };
            window.API.post("comments", newComment).then(res => {
                this.comments.push(res.data);
                this.commentText = null;
            }).catch(err => {
                alert(err.response.data);
            })
        },


        saveEditedComment: function (newComment) {
            window.API.put("comments", newComment).then(res => {
                this.comments = this.comments.map(c => {
                    if (c.id === newComment.id) {
                        return {...c, text: newComment.text, modifiedAt: newComment.modifiedAt};
                    }
                    return c;
                });
            }).catch(err => {
                alert(err.response.data);
            })
        },

        removeComment: function (commentToRemove) {
            window.API.delete("comments/" + commentToRemove.id).then(res => {
                this.comments = this.comments.filter(function(value) {
                   return value.id !== commentToRemove.id;
                });
            }).catch(err => {
                alert(err.response.data);
            })
        },
        deletePost: function (post) {
            this.$parent.removePost(this.post,this.textDel);
        }
    },


    template: `
<div class="d-flex justify-content-between align-items-center">
    <div class="card postCardContainer">
        <div class="card-header">
            <div class="d-flex justify-content-between align-items-center">
                <div class="d-flex justify-content-between align-items-center">
                    <div class="mr-2">
                        <img class="profileImageIconMedium" v-bind:src="user.profileImageUrl" alt="">
                    </div>
                    <div class="ml-2">
                        <div class="h5 m-0">@{{user.username}}</div>
                        <div class="h7 text-muted">{{user.name}} {{user.surname}}</div>
                        <div class="text-muted h7 mb-2"> <i class="fa fa-clock-o"></i>{{post.createdAt}}</div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="card-body">
            <p v-if="post.text !== null && post.text !== '' && post.text!=='null'" class="card-text">
                {{post.text}}
            </p>
            <div style="width: 100%;"  v-if="post.imageUrl">
                <img class="imageForPost" v-bind:src="post.imageUrl"/>
            </div>
        </div>
        <hr/>
        <div class="card-header postCommentsContainer">
            <h8>Comments({{numOfComments}})</h8>
            <hr>
            <br/>
            <div v-for="comment in comments" class="d-flex justify-content-between align-items-center">
               <comment-component :comment="comment"></comment-component>
            </div>
        </div>
        <div v-if="loggedUser != null" class="d-flex justify-content-between align-items-center postCommentsContainer">
            <div class="d-flex justify-content-between align-items-center">
                <div class="mr-2">
                    <img class="profileImageIconSmall" v-bind:src="loggedUser.imageUrl" alt="">
                </div>
                <div class="ml-2">
                    <textarea class="commentInputField" v-model="commentText" placeholder="Write comment here..."/>
                </div>
                <button v-on:click="addComment()" class="btn btn-primary">Comment</button>
            </div>
        </div>
        
        
    </div>
     <div v-if="loggedUser != null && loggedUser.role === 'ADMIN' ">
            <div class="dropdown">
                 
                <button class="btn btn-link dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                </button>
                <div class="dropdown-menu dropdown-menu-right">
                    <label>Unesi razlog brisanja:</label>
                    <textarea v-model="textDel"></textarea>
                    </br>
                    <div><button v-on:click="deletePost(post)"   class="dropdown-item deletePostMenuItem fa fa-pencil" > Delete</button></div>

                </div>
            </div>
        </div>
        
        
           

</div>
    `


});
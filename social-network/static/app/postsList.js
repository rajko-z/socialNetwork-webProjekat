Vue.component("post-list", {
    props: ['user'],
    data: function () {
        return {
            loggedUser: window.getCurrentUser(),
            canSeeContent: false,
            posts: [],
            // type of posts: all|regular|image
            postType: 'all'
        }
    },

    methods: {

        allPostsClicked: function() {
            this.postType = 'all';
            this.getAllPostsFromUser();
        },
        regularPostsClicked: function() {
            this.postType = 'regular';
            this.getRegularPostsFromUser();
        },
        imagePostsClicked: function() {
            this.postType = 'image';
            this.getImagePostsFromUser();
        },

        getAllPostsFromUser: function() {
            window.API.get("users/getPostsWithComments/" + this.user.username).then(res => {
                this.posts = res.data;
            }).catch(err => {
                alert(err.response.data);
            });
        },
        getRegularPostsFromUser: function() {
            window.API.get("users/getPostsWithComments/" + this.user.username + "?postType=regular").then(res => {
                this.posts = res.data;
            }).catch(err => {
                alert(err.response.data);
            });
        },
        getImagePostsFromUser: function() {
            window.API.get("users/getPostsWithComments/" + this.user.username + "?postType=image").then(res => {
                this.posts = res.data;
            }).catch(err => {
                alert(err.response.data);
            });
        }
    },

    mounted: function() {
        if (this.user.accountPrivate === false) {
            this.canSeeContent = true;
            this.getAllPostsFromUser();
            return;
        }
        if (window.getCurrentUser() === null) {
            this.canSeeContent = false;
            return;
        }
        if (window.getCurrentUser().username === this.user.username) {
            this.canSeeContent = true;
            this.getAllPostsFromUser();
            return;
        }

        window.API.get("friendStatus/" + this.user.username).then(res => {
            if (res.data !== 'FRIENDS') {
                this.canSeeContent = false;
            } else {
                this.canSeeContent = true;
                this.getAllPostsFromUser();
            }
        }).catch(err => {
            alert(err.response.data);
        })

    }
    ,

    template: `
<div class="postsListContainer">
    <h5 v-if="canSeeContent===false">This account is private. Become friends to see all posts and images</h5>
    <div v-else>
        <hr>
        <div class="postsListButtonContainer">
            <button v-on:click="allPostsClicked()" class="btn btn-outline-primary">All posts</button>
            <button v-on:click="regularPostsClicked()" class="btn btn-outline-primary">Regular posts</button>
            <button v-on:click="imagePostsClicked()" class="btn btn-outline-primary">Image posts</button>
        </div>
        
        <h5 v-if="postType==='all'" style="color: darkblue">All posts</h5>
        <h5 v-else-if="postType==='regular'" style="color: darkblue">Regular posts</h5>
        <h5 v-else-if="postType==='image'" style="color: darkblue">Image posts</h5>
        <hr>
        <br/>
        
        <div v-for="post in posts">
            <post-card :post="post" :user="user"></post-card>
            <br/>
        </div>
    </div>
</div>
    `


});
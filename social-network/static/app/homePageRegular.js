
Vue.component("home-page-regular", {
    data: function () {
        return {
            loggedUser: window.getCurrentUser(),
            posts: []
        }
    },


    mounted() {
        if (window.getCurrentUser() === null) {
            router.push('/');
        }
        window.API.get("users/getFeed").then(res => {
            this.posts = res.data;
        }).catch(err => {
            alert(err.response.data);
        });
    },

    template: `
<div>
    <div class="homePagePostListContainer">
        <h4>Welcome back {{loggedUser.username}} <span class="wavingHand">👋</span></h4>
        <hr>
        <div v-for="post in posts">
            <post-card :post="post" :user="post.user"></post-card>
            <br/>
            <br/>
            <br/>
        </div>
    </div>
</div>
    `
});
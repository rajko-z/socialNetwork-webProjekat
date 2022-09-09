Vue.component("home-page-admin", {
    data: function () {
        return {
            loggedUser: window.getCurrentUser(),
            posts: []
        }
    },
    methods: {
        removePost: function (postToRemove,txt) {

            window.API.delete("postsAdmin/" + postToRemove.id+"/"+txt).then(res => {
                this.posts = this.posts.filter(function(value) {
                    return value.id !== postToRemove.id;
                });
            }).catch(err => {
                alert(err.response.data);
            })
        }},
    mounted() {

        if (window.getCurrentUser() === null) {
            router.push('/');
        }
        if (window.getCurrentUser().role !== 'ADMIN') {
            alert('Unauthorized');
            router.push('homePageRegular');
        }

        window.API.get("users/getFeed").then(res => {
            this.posts = res.data;

        }).catch(err => {
            alert(err.response.data);
        });
    },

    template: `
<div>
    <h1>ADMIN HOME PAGE - {{loggedUser.username}}</h1>
    <br>
        <br>
        <br>
        <br>
        <br>
        <div v-for="post in posts">
            <post-card :post="post" :user="post.user"></post-card>
            <br/>
            <br/>
            <br/>
        </div>
</div>
    `




});

Vue.component("home-page-regular", {
    data: function () {
        return {
            loggedUser: window.getCurrentUser(),
            posts: []
        }
    },

    template: `
<div>
    <h1>REGULAR USER HOME PAGE - {{loggedUser.username}}</h1>
    <hr>
    <div v-for="p in posts">
        {{p.text}}
    </div>
</div>
    `
    ,
    mounted() {
        if (window.getCurrentUser() === null) {
            router.push('/');
        }
        window.API.get("users/getFeed").then(res => {
            this.posts = res.data;
        }).catch(err => {
           alert(err.response.data);
        });
    }

});
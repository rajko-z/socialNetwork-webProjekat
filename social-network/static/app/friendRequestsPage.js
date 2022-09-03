Vue.component("friend-request-page", {
    data: function () {
        return {
            friendRequests: [],
            pendingRequests: [],
            acceptActive: true
        }
    },

    computed: {
        friendRequestsSize() {
            return this.friendRequests.length;
        },
        pendingRequestsSize() {
            return this.pendingRequests.length;
        }
    },

    methods: {
        loadFriendRequests: function () {
            window.API.get("friendStatus/getAllFriendRequests").then(res => {
                this.friendRequests = res.data;
                this.acceptActive = true;
            }).catch(err => {
                alert(err.response.data);
            });
        },
        loadPendingRequests: function () {
            window.API.get("friendStatus/getAllPendingFriendRequestsThatISent").then(res => {
                this.pendingRequests = res.data;
                this.acceptActive = false;
            }).catch(err => {
                alert(err.response.data);
            });
        }
    },

    mounted: function() {
        if (window.getCurrentUser() === null) {
            router.push('/');
        }
        this.loadFriendRequests();
    }
    ,

    template: `
<div>
    <button title="Show all friend requests" type="button" v-on:click="loadFriendRequests()" class="btn-primary btn">Friend Requests</button>
    <button title="Show all pending requests" type="button" v-on:click="loadPendingRequests()" class="btn-primary btn">Pending Requests</button>

    <div v-if="acceptActive">
        <h5 class="friendRequestTextHeader">You have <b>{{friendRequestsSize}}</b> friend requests</h5>
        <hr>
        <div class="friendRequestContainer" v-for="fr in friendRequests">
            <label>{{fr.createdAt}}</label>
            <user-info :user="fr.from" view="small"></user-info>
        </div>
        <br/>
    </div>
    <div v-else>
         <h5 class="friendRequestTextHeader">You sent <b>{{pendingRequestsSize}}</b> friend requests</h5>
         <hr>
         <div class="friendRequestContainer" v-for="fr in pendingRequests">
            <label>{{fr.createdAt}}</label>
            <user-info :user="fr.to" view="small"></user-info>
        </div>
        <br/>
    </div>
</div>
    `


});
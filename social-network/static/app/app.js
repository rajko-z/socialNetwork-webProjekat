const Login = { template: '<login></login>' }
const Register = {template: '<register></register>'}
const HomePageRegular = {template: '<home-page-regular></home-page-regular>'}
const HomePageAdmin = {template: '<home-page-admin></home-page-admin>'}
const HomePageGuest = {template: '<home-page-guest></home-page-guest>'}
const ChangePassword = {template: '<change-password></change-password>'}
const EditProfilePage = {template: '<edit-profile-page></edit-profile-page>'}
const UserProfilePage = {template: '<user-profile-page></user-profile-page>'}
const FriendRequestPage = {template: '<friend-request-page></friend-request-page>'}

const router = new VueRouter({
	mode: 'hash',
	routes: [
		{ path: '/', component: Login},
		{ path: '/login', component: Login},
		{ path: '/registration', component: Register },
		{ path: '/homePageRegular', component: HomePageRegular},
		{ path: '/homePageAdmin', component: HomePageAdmin},
		{ path: '/homePageGuest', component: HomePageGuest},
		{ path: '/changePassword', component: ChangePassword},
		{ path: '/editProfilePage', component: EditProfilePage},
		{ path: '/userProfilePage:username', name: 'userProfilePage', component: UserProfilePage},
		{ path: '/friendRequestPage', component: FriendRequestPage}
	]
});

const app = new Vue({
	router,
	el: '#socialNetwork',
	data: {
		loggedUser: window.getCurrentUser()
	},
	mounted() {
		this.$root.$on('userLoggedIn', () => {
			this.loggedUser = window.getCurrentUser()
		});
	},
	methods: {
		homePage: function() {
			if (window.getCurrentUser().role === 'REGULAR') {
				router.push('homePageRegular');
			} else {
				router.push('homePageAdmin');
			}
		},
		reg: function() {
			router.push('registration');
		},
		logOut: function() {
			localStorage.clear();
			this.loggedUser = null;
			router.push('/');
		},
		changePassword: function() {
			router.push('changePassword');
		},
		editProfilePage: function () {
			router.push('editProfilePage');
		},
		friendRequestPage: function() {
			router.push('friendRequestPage');
		},
		userProfilePage: function () {
			let username = window.getCurrentUser().username;
			router.push({
				name: 'userProfilePage',
				params: {
					username: username
				}
			});
			this.$router.go();
		}

	}
});




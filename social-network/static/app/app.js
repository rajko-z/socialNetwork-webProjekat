const Login = { template: '<login></login>' }
const Register = {template: '<register></register>'}
const HomePageRegular = {template: '<home-page-regular></home-page-regular>'}
const HomePageAdmin = {template: '<home-page-admin></home-page-admin>'}
const HomePageGuest = {template: '<home-page-guest></home-page-guest>'}

const router = new VueRouter({
	mode: 'hash',
	routes: [
		{ path: '/', component: Login},
		{ path: '/registration', component: Register },
		{ path: '/homePageRegular', component: HomePageRegular},
		{ path: '/homePageAdmin', component: HomePageAdmin},
		{ path: '/homePageGuest', component: HomePageGuest}
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
		logOut: function() {
			localStorage.clear();
			this.loggedUser = null;
			router.push('/');
		}
	}
});




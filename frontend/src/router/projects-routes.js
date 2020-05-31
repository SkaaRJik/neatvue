const projects_routes = [
    {
        path: '/projects',
        name: 'projects',
        component: () => import(/* webpackChunkName: "about" */ '../views/Projects.vue'),
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/projects/shared',
        name: 'shared-projects',
        component: () => import(/* webpackChunkName: "about" */ '../views/Projects.vue'),
        props: {shared: true},
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/projects/new',
        name: 'new-project',
        component: () => import(/* webpackChunkName: "about" */ '../views/NewProject.vue'),
        meta: {
            requiresAuth: true
        }
    },
]

export default projects_routes;


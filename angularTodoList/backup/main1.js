'use strict';

var projectApp = angular.module('project', ['ngResource']);

projectsApp.config(function($routeProvider){
	$routeProvider.
		when('/', {
			controller: 'ProjectListCtrl as list',
			templateUrl: 'projectlist.html'}).
		when('/project/:id', {
			controller: 'ProjectDetailCtrl as detail',
			templateUrl: 'projectlist.html'
		});
});

projectsApp.factory('Project', function($resource){
	return $resource('/project/:id', {id: '@id'});
});

projectApp.controller('ProjectListCtrl', function(Project){
	this.projects = Project.query();
});

projectsApp.controller('ProjectDetailCtrl', function(Project, $routeParams){
	this.project = $routeParams.id
		? Project.get({id: $routeParams.id});
		: new Project();

	this.save = function(){
		this.project.$save(goHome);
	};

	this.delete = function(){
		this.project.$delete(goHome);
	};

	function goHome(){
		$location.path('/');
	};
});
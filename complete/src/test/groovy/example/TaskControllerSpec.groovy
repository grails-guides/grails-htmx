package example

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class TaskControllerSpec extends Specification implements ControllerUnitTest<TaskController>, DomainUnitTest<Task> {

    void setup() {
        mockDomain(Task)
    }

    // --- index ---

    void "index renders the task list with model"() {
        given:
        new Task(title: 'First').save(flush: true)
        new Task(title: 'Second').save(flush: true)

        when:
        controller.index()

        then:
        response.status == 200
        model.tasks.size() == 2
        model.task instanceof Task
        model.q == ''
    }

    void "index shows empty list when no tasks exist"() {
        when:
        controller.index()

        then:
        response.status == 200
        model.tasks.isEmpty()
    }

    // --- show ---

    void "show returns task fragment for existing task"() {
        given:
        def task = new Task(title: 'Visible').save(flush: true)

        when:
        controller.show(task.id)

        then:
        response.status == 200
        response.text.contains('Visible')
    }

    void "show returns 404 for missing task"() {
        when:
        controller.show(999)

        then:
        response.status == 404
    }

    // --- search ---

    void "search returns matching tasks"() {
        given:
        new Task(title: 'Alpha').save(flush: true)
        new Task(title: 'Beta').save(flush: true)

        when:
        params.q = 'Alpha'
        controller.search()

        then:
        response.status == 200
        response.text.contains('Alpha')
        !response.text.contains('Beta')
    }

    void "search with empty query returns all tasks"() {
        given:
        new Task(title: 'Alpha').save(flush: true)
        new Task(title: 'Beta').save(flush: true)

        when:
        params.q = ''
        controller.search()

        then:
        response.status == 200
        response.text.contains('Alpha')
        response.text.contains('Beta')
    }

    void "search with no match returns empty fragment"() {
        given:
        new Task(title: 'Alpha').save(flush: true)

        when:
        params.q = 'ZZZZ'
        controller.search()

        then:
        response.status == 200
        response.text.contains('No tasks yet')
    }

    // --- create ---

    void "create saves a valid task and renders the created template"() {
        when:
        request.method = 'POST'
        params.title = 'New Task'
        controller.create()

        then:
        Task.count() == 1
        Task.first().title == 'New Task'
        response.status == 200
    }

    void "create returns 422 for blank title"() {
        when:
        request.method = 'POST'
        params.title = ''
        controller.create()

        then:
        Task.count() == 0
        response.status == 422
    }

    void "create returns 422 for null title"() {
        when:
        request.method = 'POST'
        // params.title not set
        controller.create()

        then:
        Task.count() == 0
        response.status == 422
    }

    // --- editForm ---

    void "editForm renders edit template for existing task"() {
        given:
        def task = new Task(title: 'Editable').save(flush: true)

        when:
        controller.editForm(task.id)

        then:
        response.status == 200
        response.text.contains('Editable')
    }

    void "editForm returns 404 for missing task"() {
        when:
        controller.editForm(999)

        then:
        response.status == 404
    }

    // --- update ---

    void "update saves new title and renders task template"() {
        given:
        def task = new Task(title: 'Original').save(flush: true)

        when:
        request.method = 'PATCH'
        params.title = 'Updated'
        controller.update(task.id)

        then:
        Task.get(task.id).title == 'Updated'
        response.status == 200
        response.text.contains('Updated')
    }

    void "update returns 404 for missing task"() {
        when:
        request.method = 'PATCH'
        params.title = 'Anything'
        controller.update(999)

        then:
        response.status == 404
    }

    void "update returns 422 for blank title"() {
        given:
        def task = new Task(title: 'Original').save(flush: true)

        when:
        request.method = 'PATCH'
        params.title = ''
        controller.update(task.id)

        then:
        response.status == 422
    }

    // --- toggle ---

    void "toggle flips done from false to true"() {
        given:
        def task = new Task(title: 'Toggle me', done: false).save(flush: true)

        when:
        request.method = 'POST'
        controller.toggle(task.id)

        then:
        Task.get(task.id).done
        response.status == 200
    }

    void "toggle flips done from true to false"() {
        given:
        def task = new Task(title: 'Toggle me', done: true).save(flush: true)

        when:
        request.method = 'POST'
        controller.toggle(task.id)

        then:
        !Task.get(task.id).done
        response.status == 200
    }

    void "toggle returns 404 for missing task"() {
        when:
        request.method = 'POST'
        controller.toggle(999)

        then:
        response.status == 404
    }

    // --- delete ---

    void "delete removes the task and renders empty state when last task"() {
        given:
        def task = new Task(title: 'Delete me').save(flush: true)

        when:
        request.method = 'DELETE'
        controller.delete(task.id)

        then:
        Task.count() == 0
        response.status == 200
        response.text.contains('No tasks yet')
    }

    void "delete returns 404 for missing task"() {
        when:
        request.method = 'DELETE'
        controller.delete(999)

        then:
        response.status == 404
    }
}

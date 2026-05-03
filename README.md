# grails-htmx

Sample app for the apache/grails-static-website guide [grails-htmx/v8](https://grails.apache.org/guides/grails-htmx/8/guide/index.html).

A worked example of HTMX-driven interactive UI on a Grails 8 GSP backend. Server-rendered initial page, then HTMX-driven inline editing, live search, optimistic delete with confirm, and toggle - no SPA, no JSON.

`initial/` is a vanilla Grails 8 starter. `complete/` is the same starter with the Task domain, controller actions returning HTML fragments, and HTMX-driven GSP partials added.

```bash
git clone -b grails8 https://github.com/grails-guides/grails-htmx.git
cd grails-htmx/complete
./gradlew bootRun
# open http://localhost:8080/tasks
```

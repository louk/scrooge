private[this] val _{{name}}Scope = scopedStats.scope("{{name}}")
private[this] val _{{name}}RequestsCounter = _{{name}}Scope.counter("requests")
private[this] val _{{name}}SuccessCounter = _{{name}}Scope.counter("success")
private[this] val _{{name}}FailuresCounter = _{{name}}Scope.counter("failures")
private[this] val _{{name}}FailuresScope = _{{name}}Scope.scope("failures")

{{functionDecl}} = {
  _{{name}}RequestsCounter.incr()
  encodeRequest("{{name}}", {{localName}}_args({{argNames}})) flatMap { this.service } flatMap {
    decodeResponse(_, {{localName}}_result.decoder)
  } flatMap { result =>
    val exception =
      {{#hasThrows}}({{#throws}}result.{{name}}{{/throws| orElse }}).map(Future.exception){{/hasThrows}}
      {{^hasThrows}}None{{/hasThrows}}

    {{#void}}Future.Done{{/void}}
    {{^void}}
      exception.orElse({{^void}}result.success.map(Future.value){{/void}})
      .getOrElse(missingResult("{{name}}"))
    {{/void}}
  } rescue {
    case ex: SourcedException =>
      this.serviceName foreach { ex.serviceName = _ }
      Future.exception(ex)
  } onSuccess { _ =>
    _{{name}}SuccessCounter.incr()
  } onFailure { ex =>
    _{{name}}FailuresCounter.incr()
    _{{name}}FailuresScope.counter(ex.getClass.getName).incr()
  }
}

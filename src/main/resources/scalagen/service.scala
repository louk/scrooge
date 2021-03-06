// ----- {{name}}

object {{name}} {
  trait Iface {{syncExtends}}{
{{#syncFunctions}}
    {{function}}
{{/syncFunctions}}
  }

  trait FutureIface {{asyncExtends}}{
{{#asyncFunctions}}
    {{function}}
{{/asyncFunctions}}
  }

{{functionStructs}}
{{finagleClient}}
{{finagleService}}
{{ostrichServer}}
}

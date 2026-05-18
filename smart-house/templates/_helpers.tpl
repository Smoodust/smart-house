{{- define "smart-house.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "smart-house.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}

{{- define "smart-house.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "smart-house.labels" -}}
helm.sh/chart: {{ include "smart-house.chart" . }}
app.kubernetes.io/name: {{ include "smart-house.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/part-of: smart-house
{{- end -}}

{{- define "smart-house.selectorLabels" -}}
app.kubernetes.io/name: {{ include "smart-house.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Revista, RevistaPayload } from '../../core/models';

@Component({
  selector: 'app-magazine-form', imports: [ReactiveFormsModule],
  template: `
    <form class="resource-form" [formGroup]="form" (ngSubmit)="submit()"><h2>{{ magazine ? 'Editar revista' : 'Nueva revista' }}</h2>
      <div class="form-grid"><label>Título<input formControlName="titulo" /></label><label>Editorial<input formControlName="editorial" /></label><label>ISSN<input formControlName="issn" /></label><label>Precio<input type="number" step="0.01" formControlName="precio" /></label><label>Stock<input type="number" formControlName="stock" /></label></div>
      @if (hasError('titulo')) { <small class="field-error">El título debe tener entre 2 y 120 caracteres.</small> }
      @if (hasError('editorial')) { <small class="field-error">La editorial debe tener entre 2 y 120 caracteres.</small> }
      @if (hasError('issn')) { <small class="field-error">El ISSN debe tener 8 o 9 caracteres.</small> }
      @if (hasError('precio') || hasError('stock')) { <small class="field-error">Precio y stock deben ser valores iguales o mayores que cero.</small> }
      <div class="form-actions"><button class="primary">{{ magazine ? 'Guardar cambios' : 'Crear revista' }}</button><button type="button" class="secondary" (click)="cancel.emit()">Cancelar</button></div>
    </form>
  `
})
export class MagazineFormComponent implements OnChanges {
  @Input() magazine: Revista | null = null; @Output() save = new EventEmitter<RevistaPayload>(); @Output() cancel = new EventEmitter<void>();
  readonly form = new FormBuilder().nonNullable.group({ titulo: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(120)]], editorial: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(120)]], issn: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(9)]], precio: [0, [Validators.required, Validators.min(0)]], stock: [0, [Validators.required, Validators.min(0)]] });
  ngOnChanges(changes: SimpleChanges) { if (changes['magazine']) this.form.reset(this.magazine ?? { titulo: '', editorial: '', issn: '', precio: 0, stock: 0 }); }
  hasError(name: keyof RevistaPayload) { const control = this.form.controls[name]; return control.invalid && (control.touched || control.dirty); }
  submit() { if (this.form.invalid) { this.form.markAllAsTouched(); return; } this.save.emit(this.form.getRawValue()); }
}

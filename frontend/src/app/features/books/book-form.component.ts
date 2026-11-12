import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Libro, LibroPayload } from '../../core/models';

@Component({
  selector: 'app-book-form', imports: [ReactiveFormsModule],
  template: `
    <form class="resource-form" [formGroup]="form" (ngSubmit)="submit()"><h2>{{ book ? 'Editar libro' : 'Nuevo libro' }}</h2>
      <div class="form-grid"><label>Título<input formControlName="titulo" /></label><label>Autor<input formControlName="autor" /></label><label>ISBN<input formControlName="isbn" /></label><label>Precio<input type="number" step="0.01" formControlName="precio" /></label><label>Stock<input type="number" formControlName="stock" /></label></div>
      @if (hasError('titulo')) { <small class="field-error">El título debe tener entre 2 y 120 caracteres.</small> }
      @if (hasError('autor')) { <small class="field-error">El autor debe tener entre 2 y 120 caracteres.</small> }
      @if (hasError('isbn')) { <small class="field-error">El ISBN debe tener entre 10 y 17 caracteres.</small> }
      @if (hasError('precio') || hasError('stock')) { <small class="field-error">Precio y stock deben ser valores iguales o mayores que cero.</small> }
      <div class="form-actions"><button class="primary">{{ book ? 'Guardar cambios' : 'Crear libro' }}</button><button type="button" class="secondary" (click)="cancel.emit()">Cancelar</button></div>
    </form>
  `
})
export class BookFormComponent implements OnChanges {
  @Input() book: Libro | null = null; @Output() save = new EventEmitter<LibroPayload>(); @Output() cancel = new EventEmitter<void>();
  readonly form = new FormBuilder().nonNullable.group({ titulo: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(120)]], autor: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(120)]], isbn: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(17)]], precio: [0, [Validators.required, Validators.min(0)]], stock: [0, [Validators.required, Validators.min(0)]] });
  ngOnChanges(changes: SimpleChanges) { if (changes['book']) this.form.reset(this.book ?? { titulo: '', autor: '', isbn: '', precio: 0, stock: 0 }); }
  hasError(name: keyof LibroPayload) { const control = this.form.controls[name]; return control.invalid && (control.touched || control.dirty); }
  submit() { if (this.form.invalid) { this.form.markAllAsTouched(); return; } this.save.emit(this.form.getRawValue()); }
}

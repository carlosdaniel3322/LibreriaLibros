import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private timeoutId?: ReturnType<typeof setTimeout>;
  readonly current = signal<string | null>(null);

  error(message: string): void {
    this.clear();
    this.current.set(message);
    this.timeoutId = setTimeout(() => this.current.set(null), 6000);
  }

  clear(): void {
    if (this.timeoutId) clearTimeout(this.timeoutId);
    this.timeoutId = undefined;
    this.current.set(null);
  }
}

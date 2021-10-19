using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SetList<T> : IList<T> {


    public SetList() {
        content = new List<T>();
    }
    public SetList(int initialsize) {
        content = new List<T>(initialsize);
    }

    private List<T> content;
    public T this[int index] { get => content[index]; set => content[index] = value; }

    public int Count => content.Count;

    public bool IsReadOnly => false;

    public void Add(T item) {
        content.Add(item);
    }

    public void Clear() {
        content.Clear();
    }

    public bool Contains(T item) {
        return content.Contains(item);
    }

    public void CopyTo(T[] array, int arrayIndex) {
        content.CopyTo(array, arrayIndex);
    }

    public IEnumerator<T> GetEnumerator() {
        return content.GetEnumerator();
    }

    public int IndexOf(T item) {
        return content.IndexOf(item);
    }

    public void Insert(int index, T item) {
        content.Insert(index, item);
    }

    public bool Remove(T item) {
        return content.Remove(item);
    }

    public void RemoveAt(int index) {
        RemoveAt(index);
    }

    IEnumerator IEnumerable.GetEnumerator() {
        return content.GetEnumerator();
    }
}

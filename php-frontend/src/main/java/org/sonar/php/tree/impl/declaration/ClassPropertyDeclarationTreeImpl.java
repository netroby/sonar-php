/*
 * SonarQube PHP Plugin
 * Copyright (C) 2010 SonarSource and Akram Ben Aissi
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.php.tree.impl.declaration;

import java.util.Iterator;
import java.util.List;

import org.sonar.php.tree.impl.PHPTree;
import org.sonar.php.tree.impl.SeparatedList;
import org.sonar.php.tree.impl.lexical.InternalSyntaxToken;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.ClassPropertyDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.VariableDeclarationTree;
import org.sonar.plugins.php.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.php.api.visitors.TreeVisitor;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

public class ClassPropertyDeclarationTreeImpl extends PHPTree implements ClassPropertyDeclarationTree {

  private final Kind kind;
  private final List<SyntaxToken> modifierTokens;
  private final SeparatedList<VariableDeclarationTree> declarations;
  private final InternalSyntaxToken eosToken;

  private ClassPropertyDeclarationTreeImpl(
    Kind kind,
    List<SyntaxToken> modifierTokens, 
    SeparatedList<VariableDeclarationTree> declarations, 
    InternalSyntaxToken eosToken
    ) {
    this.kind = kind;
    this.modifierTokens = modifierTokens;
    this.declarations = declarations;
    this.eosToken = eosToken;
  }
  
  public static ClassPropertyDeclarationTree variable(List<SyntaxToken> modifierTokens, SeparatedList<VariableDeclarationTree> declarations, InternalSyntaxToken eosToken) {
    return new ClassPropertyDeclarationTreeImpl(Kind.CLASS_PROPERTY_DECLARATION, modifierTokens, declarations, eosToken);
  }

  public static ClassPropertyDeclarationTree constant(SyntaxToken constToken, SeparatedList<VariableDeclarationTree> declarations, InternalSyntaxToken eosToken) {
    return new ClassPropertyDeclarationTreeImpl(Kind.CLASS_CONSTANT_PROPERTY_DECLARATION, ImmutableList.of(constToken), declarations, eosToken);
  }

  @Override
  public List<SyntaxToken> modifierTokens() {
    return modifierTokens;
  }

  @Override
  public SeparatedList<VariableDeclarationTree> declarations() {
    return declarations;
  }

  @Override
  public SyntaxToken eosToken() {
    return eosToken;
  }

  @Override
  public Kind getKind() {
    return kind;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.concat(
      modifierTokens.iterator(),
      declarations.elementsAndSeparators(Functions.<VariableDeclarationTree>identity()),
      Iterators.singletonIterator(eosToken));
  }

  @Override
  public void accept(TreeVisitor visitor) {
    visitor.visitClassPropertyDeclaration(this);
  }

}
